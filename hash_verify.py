import sys
from web3 import Web3  

#print  (sys.argv[1]) 
#infura_url = 'https://holesky.infura.io/v3/5cb88f299e974e9082c695c5fb3e9b13'  
infura_url = 'https://sepolia.infura.io/v3/5cb88f299e974e9082c695c5fb3e9b13'
web3 = Web3(Web3.HTTPProvider(infura_url))  
  
print(web3.eth.get_transaction(sys.argv[1]))

#print(web3.eth.get_transaction("0x69ee9128641fdf21a30be25dc1417ab7fdbe388b33c3187c566f19ee92be8126")['input'])

