import argparse
import base64
import io

if __name__ == '__main__':
	parser = argparse.ArgumentParser()
	parser.add_argument('--image64', required=True)
	option = parser.parse_args()
	temp = io.BytesIO(base64.b64decode(option.image64))
	print(temp)