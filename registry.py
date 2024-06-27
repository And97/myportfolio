from web3 import Web3, exceptions  
import sys
import random
 

 
infura_url = 'https://holesky.infura.io/v3/5cb88f299e974e9082c695c5fb3e9b13'  
private_key = '#' #metamask
from_account = '0x3403974b2c466A7Fc34EEBb607294f72C8CF49F3'  #metamask
to_account = '0x00C007CFf2bAe0b56d667e5ce421FBA83B007'+ str(random.randint(100, 999))  # random
web3 = Web3(Web3.HTTPProvider(infura_url))  
  
try:  
    from_account = web3.to_checksum_address(from_account)  
except exceptions.InvalidAddress:  
    print(f"Invalid 'from_account' address: {from_account}")  
  
try:  
    to_account = web3.to_checksum_address(to_account)  
except exceptions.InvalidAddress:  
    print(f"Invalid 'to_account' address: {to_account}")  
    
    
data = sys.argv[1]+";"+sys.argv[2]+";"+sys.argv[3]
nonce = web3.eth.get_transaction_count(from_account)  
tx = {
    'type': '0x2',
    'nonce': nonce,
    'from': from_account,
    'to': to_account,
    'value': web3.to_wei(0, 'ether'),
    'maxFeePerGas': web3.to_wei('0', 'gwei'),
    'maxPriorityFeePerGas': web3.to_wei('0', 'gwei'),
    'chainId': 17000,
    'data' : data.encode("utf-8").hex()
}
gas = web3.eth.estimate_gas(tx)
tx['gas'] = gas
signed_tx = web3.eth.account.sign_transaction(tx, private_key)
tx_hash = web3.eth.send_raw_transaction(signed_tx.rawTransaction)
print(str(web3.to_hex(tx_hash)))
