import argparse
import base64
import io
import cv2

def predict(base64string):
	# temp = io.BytesIO(base64.b64decode(base64string))
	image = cv2.imread("image//test.png", cv2.IMREAD_COLOR)
	cv2.imshow("test", image)
	cv2.waitKey(0)
	cv2.destroyAllWindows()
	print("done")
if __name__ =="__main__":
    predict("te")