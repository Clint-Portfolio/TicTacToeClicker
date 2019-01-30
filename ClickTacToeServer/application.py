""""
Flask server setup for "ClickTacToe"
There are 2 pages, 'request' and 'handler'
'request' works in the request database, adding and removing game requests.
"""


import os

from cs50 import SQL
from flask import Flask, request, Response, json
from werkzeug.exceptions import default_exceptions

from helpers import apology
import random

# # Ensure environment variable is set
# if not os.environ.get("API_KEY"):
#     raise RuntimeError("API_KEY not set")

# Configure application
app = Flask(__name__)

# Configure CS50 Library to use SQLite database
db = SQL("sqlite:///ClickTacToeDB.db")


"""
There are 3 types of game requests in '/ClickTacToeGameRequest': regular request, confirm request and cancel request
regular requests are requests that the app sends every 5 seconds, and the server will respond with either a game ID or 0, to signify no game has been found.
confirm requests are sent to confirm a game has been found
cancel requests are requests sent to tell the database to close the request for a game.
"""
@app.route("/ClickTacToeGameRequest", methods=["POST"])
def request_game():
    # get JSON object from post
    content = request.get_json()
    print(f"\n\n new Request: {content}\n")

    username = content["playerID"]

    last_move = -1
    game_id = 0
    status = "in_progress"
    starting_player = ""
    gridSize = content["gridSize"]

    if content["status"] == "looking_for_game":
        available_games = db.execute("SELECT gameID, \
                                             COALESCE(player1, player2) AS 'names' \
                                      FROM games \
                                      WHERE status = 'looking_for_game';")

        if content["playerID"] not in [i["names"] for i in available_games]:
            if len(available_games) > 0:
                db.execute("UPDATE 'games' \
                            SET \
                               player1 = CASE WHEN player1 IS NULL THEN :username ELSE player1 END, \
                               player2 = CASE WHEN player2 IS NULL THEN :username ELSE player2 END, \
                               lastMove = -1, \
                               status = 'player1' \
                            WHERE gameID = :gameID;", username=username, gameID=available_games[0]['gameID'])
                game_data = (db.execute("SELECT player1, gameID, gridSize FROM games WHERE gameID = :gameID", gameID=available_games[0]['gameID']))[0]
                print(f"gamedata: {game_data}")
                starting_player = game_data["player1"]
                game_id = game_data["gameID"]
                gridSize = game_data["gridSize"]
                print(f"\n\ngame id = {game_id}\n\n")
            else:
                if random.random() > 0.5:
                    db.execute("INSERT INTO 'games' VALUES (NULL, :username, NULL, NULL, 'looking_for_game', :gridSize)", username=username, gridSize=gridSize)
                else:
                    db.execute("INSERT INTO 'games' VALUES (NULL, NULL, :username, NULL, 'looking_for_game', :gridSize)", username=username, gridSize=gridSize)

    elif content["status"] == "waiting_for_game":
        all_players_dict = db.execute("SELECT gameID, player1, player2 \
                                      FROM games \
                                      WHERE (player1 = :username \
                                            OR player2 = :username) \
                                            AND status = 'player1';", username=content["playerID"])
        print(f"\n\n{all_players_dict}\n\n")
        player_1_list = []
        player_2_list = []
        gameID_list = []

        for i in all_players_dict:
            player_1_list.append(i["player1"])
            player_2_list.append(i["player2"])
            gameID_list.append(i["gameID"])

        print(f"\nplayer 1 list: {player_1_list}")
        print(f"\nplayer 2 list: {player_2_list}")

        if content["playerID"] in player_1_list:
            game_id = gameID_list[player_1_list.index(content["playerID"])]
            starting_player = content["playerID"]

        elif content["playerID"] in player_2_list:
            game_id = gameID_list[player_2_list.index(content["playerID"])]
            starting_player = "The other one"

    elif content["status"] == "cancel":
        db.execute("DELETE FROM 'games' \
                    WHERE (player1 = :username \
                        OR player2 = :username \
                    AND status = 'waiting_for_game')", username=content["playerID"])

    json_object = json.dumps({"gameID" : game_id, "gridSize" : gridSize, "startingPlayer": starting_player}, ensure_ascii = False)
    print(json_object)
    print("\n\n")
    # make sure the JSON is UTF-8 compliant
    response = Response(json_object, content_type="application/json; charset=UTF-8")
    return response

"""
A Moverequest JSON consists of 2 parts:
gameID, the game ID
lastMove, the last move played

the server will then post a JSON with 3 elements:
gameID, the game ID,
lastMove, the last move played
status, which is either "player1" or "player2"
"""
@app.route("/ClickTacToeMoveHandler", methods=["POST"])
def request_move():
    content = request.get_json()
    gameID = content["gameID"]
    lastMove = content["lastMove"]
    print(content)
    current_status = (db.execute("SELECT * FROM 'games' WHERE gameID = :gameID", gameID=gameID))[0]

    if current_status["status"] == "finished":
        pass
    elif current_status["lastMove"] != lastMove and lastMove >= 0:
        db.execute("UPDATE 'games' \
                    SET \
                        lastMove = :lastMove, \
                        status = CASE WHEN status IS 'player1' THEN 'player2' ELSE status END \
                    WHERE gameID = :gameID;", lastMove=lastMove, gameID=gameID)
    elif lastMove == -2:
        db.execute("UPDATE 'games' \
                    SET 'status' = 'finished' \
                    WHERE gameID = :gameID", gameID=gameID)
    elif lastMove == -3:
        db.execute("UPDATE 'games' \
                    SET 'status' = CASE WHEN status IS NOT 'finished' THEN 'canceled' ELSE status END \
                    WHERE gameID = :gameID", gameID=gameID)
    current_status = (db.execute("SELECT * FROM 'games' \
                                     WHERE gameID = :gameID;", gameID=gameID))[0]
    json_object = json.dumps(current_status)
    response = Response(json_object, content_type="application/json; charset=UTF-8")
    return response

def errorhandler(e):
    """Handle error"""
    return apology(e.name, e.code)


# listen for errors
for code in default_exceptions:
    app.errorhandler(code)(errorhandler)
