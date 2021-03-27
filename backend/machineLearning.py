import faceRecog


def recognize(base64string):
    faceRecog.get_landmarks(base64string, rotate=True)
    return 0


def return1():
    return 1
