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


def deserialize_string(string, c):
    string_arr = string.split('~')

    r = len(string_arr) - 1

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


def upload_class_file(new_class_arr):
    storage_client = storage.Client()
    bucket = storage_client.bucket('live-link-308404.appspot.com')
    blob = bucket.blob('/class')

    string = serialize_matrix(new_class_arr)

    blob.upload_from_string(string)


def download_class_file():
    storage_client = storage.Client()
    bucket = storage_client.bucket('live-link-308404.appspot.com')
    blob = bucket.blob('/class')

    string = blob.download_as_string().decode('utf-8')
    return deserialize_string(string, 2)


def get_next_class():
    class_arr = download_class_file()
    class_num = None
    try:
        class_num = class_arr[-1, 0]
        class_num += 1
    except:
        class_num = 0
    return class_num


def insert_new_class(class_id, person_id):
    class_arr = download_class_file()
    insert_arr = np.array([class_id, person_id])
    new_class_arr = np.vstack(class_arr, insert_arr)

    upload_class_file(new_class_arr)


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

    array = deserialize_string(string, 468 * 3 + 1)
    return array
