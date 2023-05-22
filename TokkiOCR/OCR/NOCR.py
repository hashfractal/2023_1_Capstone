import io
import requests
import uuid
import time
import json
from PIL import Image

def NOCR(image):
	api_url = 'https://qrf89724t1.apigw.ntruss.com/custom/v1/22697/6ed3afe25f53c78a536f8620d49e7d1c69beea930a4e1f6ec404cd3720ef4966/general'
	secret_key = 'U3R1ZVZHbnphRFFtT05sQm9aUVZqZEVyTEpiWmRZTUo='

	buffer = io.BytesIO()
	image.save(buffer, format='JPEG')
	request_json = {
		'images': [
			{
				'format': 'jpg',
				'name': 'demo'
			}
		],
		'requestId': str(uuid.uuid4()),
		'version': 'V2',
		'timestamp': int(round(time.time() * 1000))
	}

	payload = {'message': json.dumps(request_json).encode('UTF-8')}
	files = [
	  ('file', buffer.getvalue())
	]
	headers = {
	  'X-OCR-SECRET': secret_key
	}

	response = requests.request("POST", api_url, headers=headers, data = payload, files = files)

	result = response.json()
	with open('result.json', 'w', encoding='utf-8') as make_file:
		json.dump(result, make_file, indent="\t", ensure_ascii=False)
		text = ""
		for field in result['images'][0]['fields']:
			text += field['inferText']
		return text