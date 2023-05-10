import OCR
from flask import Flask, request
from PIL import Image
from io import BytesIO
import base64
app = Flask(__name__)

@app.route("/test", methods=['POST'])
def test():
    params = request.get_data().decode("utf-8")
    img = Image.open(BytesIO(base64.b64decode(str(params))))
    return OCR.OCR(img)


if __name__ == "__main__":
    app.run(debug=True, host='0.0.0.0', port=8080)