import cv2 as vision
import mediapipe as mp
import numpy as np
import base64

# mp_drawing = mp.solutions.drawing_utils
mp_face_mesh = mp.solutions.face_mesh


def decode_base64_to_img(encoded):
    arr = np.fromstring(base64.b64decode(encoded), np.uint8)
    return vision.imdecode(arr, vision.IMREAD_COLOR)


def get_landmarks(base64string, rotate=False):

    mp_face_mesh = mp.solutions.face_mesh
    # drawing_spec = mp_drawing.DrawingSpec(thickness=1, circle_radius=1, color=(255, 0, 0))

    img = decode_base64_to_img(base64string)
    if rotate:
        img = vision.rotate(img, vision.ROTATE_90_CLOCKWISE)
    # annotated = img.copy()
    img = vision.cvtColor(img, vision.COLOR_BGR2GRAY)

    with mp_face_mesh.FaceMesh(static_image_mode=True, max_num_faces=1, min_detection_confidence=0.5) as face_mesh:
        result = face_mesh.process(vision.cvtColor(img, vision.COLOR_BGR2RGB))
        # print(result.multi_face_landmarks)

        # for face_landmarks in result.multi_face_landmarks:
        #     mp_drawing.draw_landmarks(image=annotated, landmark_list=face_landmarks, connections=mp_face_mesh.FACE_CONNECTIONS,
        #                               landmark_drawing_spec=drawing_spec, connection_drawing_spec=drawing_spec)
        # vision.imwrite('./annotated.png', annotated)

        features = np.zeros(shape=(468*3,))
        idx = 0
        for data_point in result.multi_face_landmarks[0].landmark:
            features[idx] = data_point.x
            features[idx + 1] = data_point.y
            features[idx + 2] = data_point.z
            idx += 3

        return features


def get_features_from_file(filename, rotate=False):
    lines = []
    with open('./' + filename + '.txt', 'r') as android_file:
        lines = android_file.readlines()

    encoded = ''
    for line in lines:
        encoded += line.strip()

    return get_landmarks(encoded, rotate=rotate)


# first = get_features_from_file('encoded', rotate=True)

# second = get_features_from_file('nitin')

# third = get_features_from_file('arnav')

# fourth = get_features_from_file('gaurang')

# print(np.linalg.norm(first - second))
# print(np.linalg.norm(first - third))
# print(np.linalg.norm(first - fourth))
# print(np.linalg.norm(second - third))
