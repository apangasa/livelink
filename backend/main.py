from flask import Flask, jsonify, request
import json
import requests
from machineLearning import recognize, test_upload, test_download, get_next_class, insert_new_class
from flask_cors import CORS


app = Flask(__name__)
CORS(app)


@app.route('/', methods=['GET'])
def app_running():
    x = test_download()
    return 'The server is running!' + str(x)


@app.route('/add_face', methods=['POST'])
def add_face():
    base64_face = request.json.get('img', None)
    person_id = request.json.get('id', None)

    class_id = get_next_class()
    insert_new_class(class_id, person_id)

    return jsonify(response=True)


@app.route('/get_face', methods=['POST'])
def get_face():
    base64_face = request.json.get('img', None)

    person_id = recognize(base64_face)

    r = requests.get('http://129.213.124.196:3000/get?id=' + str(person_id))

    return r.json()


if __name__ == '__main__':
    app.run()
