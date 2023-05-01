# 학습
python train.py --train_data data_lmdb/train --valid_data data_lmdb/validation --Transformation TPS --FeatureExtraction ResNet --SequenceModeling BiLSTM --Prediction CTC --data_filtering_off --batch_size 32 --workers 0 --valInterval 1  

python train.py --train_data data_lmdb/train --valid_data data_lmdb/validation --Transformation TPS --FeatureExtraction ResNet --SequenceModeling BiLSTM --Prediction CTC --data_filtering_off --workers 0 --saved_model saved_models/TPS-ResNet-BiLSTM-Attn-E.pth --valInterval 100

# 학습 DB생성 train
python create_lmdb_dataset.py --inputPath ./dataset/ --gtFile ./dataset/gt_Train.txt --outputPath ./data_lmdb/train

python create_lmdb_dataset.py --inputPath ./dataset/ --gtFile ./dataset/gt_Validation.txt --outputPath ./data_lmdb/validation