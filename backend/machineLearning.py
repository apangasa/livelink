import faceRecog
import numpy as np
from google.cloud import storage
import logging


def recognize(base64string):
    faceRecog.get_landmarks(base64string, rotate=True)
    return 0


def serialize_matrix(matrix):
    string = ''
    for row in matrix:
        for element in row:
            string += str(element) + ' '
        string += '~'
    return string


def deserialize_string(string):
    string_arr = string.split('~')

    r = len(string_arr) - 1
    c = 468 * 3 + 1

    matrix = np.zeros(shape=(r, c))
    for row_idx, row in enumerate(string_arr):
        row_arr = row.split(' ')
        for el_idx, el in enumerate(row_arr):
            # logging.info('element is ' + el)
            if el != '':
                try:
                    matrix[row_idx, el_idx] = float(el.strip())
                except:
                    continue
    return matrix


def test_upload():
    storage_client = storage.Client()
    bucket = storage_client.bucket('live-link-308404.appspot.com')
    blob = bucket.blob('/test')

    array = np.array([[1, 2], [3, 4]])

    string = serialize_matrix(array)

    blob.upload_from_string(string)
    return 1


def test_download():
    storage_client = storage.Client()
    bucket = storage_client.bucket('live-link-308404.appspot.com')
    blob = bucket.blob('/test')

    string = blob.download_as_string().decode('utf-8')
    # logging.info('string:' + string)

    array = deserialize_string(string)
    return array
