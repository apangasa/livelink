import faceRecog
import numpy as np
from google.cloud import storage


def recognize(base64string):
    faceRecog.get_landmarks(base64string, rotate=True)
    return 0


def serialize_matrix(matrix):
    string = ''
    for row in matrix:
        for element in row:
            string += str(element) + ' '
        string += '\n'
    return string


def deserialize_string(string):
    string_arr = string.split('\n')
    matrix = np.zeros(shape=(len(string_arr, (468 * 3) + 1)))
    for row_idx, row in enumerate(string_arr):
        row_arr = row.split(' ')
        for el_idx, el in enumerate(row_arr):
            matrix[row_idx, el_idx] = el
    return matrix


def test_func():
    storage_client = storage.Client()
    bucket = storage_client.bucket('live-link-308404.appspot.com')
    blob = bucket.blob('/test')

    array = np.array([[1, 2], [3, 4]])

    string = serialize_matrix(array)

    blob.upload_from_string(string)
    return 1
