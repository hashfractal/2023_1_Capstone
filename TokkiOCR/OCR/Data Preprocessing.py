import json
import random
import pickle
import os
import cv2
import cv2_ext
from tqdm import tqdm

def imageMinimalize(group):
	imageFiles = os.listdir(f'./dataset/{group}/')
	
	for i in imageFiles:
		img = cv2_ext.imread(f'./dataset/{group}/{i}')
		img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
		img = cv2.resize(img, dsize=(200, 150), interpolation=cv2.INTER_LINEAR)
		cv2_ext.imwrite(f'./dataset/{group}_mini/{i}', img)


def createGt(group):
	# Separate dataset - train, validation, test
	imageFiles = os.listdir(f'./dataset/{group}/')
	total = len(imageFiles)

	# load Json label data
	jsonFiles = []
	jsonlist = os.listdir(f'./dataset/Label/{group}/')

	for i in jsonlist:
		jsonFiles.append(json.load(open(f'./dataset/Label/{group}/{i}', 'rt', encoding='UTF-8')))

	# Source and Label dictionary
	dataset = []
	for i in range(total - 1):
		text = ""
		for anno in jsonFiles[i]["annotations"]:
			if anno["text"] != "xxx":
				text += anno["text"]
		dataset.append(f"{group}/{imageFiles[i]}\t{text}")

	outputpath = f'./dataset/gt_{group}.txt'
	with open(outputpath,'w',encoding='UTF-8') as f:
		for i in dataset:
			f.write(i+'\n')
   
if __name__ == '__main__':
	# print("start")
	# createGt('Test')
	# createGt('Train')
	# createGt('Validation')
	# print("done")
	imageMinimalize('Validation')
	print("done")