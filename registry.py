from web3 import Web3, exceptions  
import sys
import random
from eth_account import Account

account = Account.create()
nuovo_indirizzo = account.address
 
infura_url = #
private_key = #
from_account = #
to_account=nuovo_indirizzo
web3 = Web3(Web3.HTTPProvider(infura_url))  
  
try:  
    from_account = web3.to_checksum_address(from_account)  
except exceptions.InvalidAddress:  
    print(f"Invalid 'from_account' address: {from_account}")  
  
try:  
    to_account = web3.to_checksum_address(to_account)  
except exceptions.InvalidAddress:  
    print(f"Invalid 'to_account' address: {to_account}")  
        
data =sys.argv[1]+";"+sys.argv[2]+";"+sys.argv[3]
nonce = web3.eth.get_transaction_count(from_account,'pending');
gas_price = web3.eth.gas_price
base_fee = web3.eth.fee_history(1, "latest")['baseFeePerGas'][-1]  # Base fee corrente
priority_fee = web3.to_wei(2, 'gwei')


tx = {
    #'type': '0x2',
    'nonce': nonce,
    'from': from_account,
    'to': to_account,
    'value': web3.to_wei(0.001, 'ether'),
    'chainId': 11155111,
    'data': data.encode("utf-8").hex(),
    'maxFeePerGas': base_fee + priority_fee,
    'maxPriorityFeePerGas': priority_fee,
    'gas': 32000

}
gas = web3.eth.estimate_gas(tx)
tx['gas'] = gas
signed_tx = web3.eth.account.sign_transaction(tx, private_key)
tx_hash = web3.eth.send_raw_transaction(signed_tx.rawTransaction)
print(str(web3.to_hex(tx_hash)))


