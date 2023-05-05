import argparse
import base64
import io

def predict(base64string):
	# temp = io.BytesIO(base64.b64decode(base64string))
	return len(base64string*100)