import sys
from web3 import Web3  

#print  (sys.argv[1]) 
infura_url = 'https://holesky.infura.io/v3/5cb88f299e974e9082c695c5fb3e9b13'  
web3 = Web3(Web3.HTTPProvider(infura_url))  
  
print(web3.eth.get_transaction(sys.argv[1]))
#print(web3.eth.get_transaction("0x7ddbee52e1220d334c52eb1483b5d683bda42846004ac6b41b6fd992fde83908"))

