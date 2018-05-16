# script to load word2vec embeddings in binary and save as text
from gensim.models.keyedvectors import KeyedVectors

model = KeyedVectors.load_word2vec_format('GoogleNews-vectors-negative300.bin', binary=True)
model.save_word2vec_format('GoogleNews-vectors-negative300.txt', binary=False)