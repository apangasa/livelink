from flask import Flask, jsonify, request
import json
from machineLearning import recognize, test_upload, test_download
from flask_cors import CORS


app = Flask(__name__)
CORS(app)


@app.route('/', methods=['GET'])
def app_running():
    x = test_download()
    return 'The server is running!' + str(x)


@app.route('/add_face', methods=['POST'])
def add_face():
    content = request.json
    resp = None
    return jsonify(response=resp)


@app.route('/get_face', methods=['POST'])
def get_face():
    base64_face = request.json.get('img', None)

    person_id = recognize(base64_face)

    return jsonify(id=person_id)


if __name__ == '__main__':
    app.run()
