import NOCR
import OCR
from flask import Flask, request
from PIL import Image
from io import BytesIO
import base64
app = Flask(__name__)


if __name__ == "__main__":
    print(OCR.OCR(Image.open('B:\\Work Space B\\2023_1_Capstone\\TokkiOCR\\OCR\\demo_image\\demo_11.jpg')))