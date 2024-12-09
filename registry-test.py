from web3 import Web3  
import datetime
def prova():
    # Connessione a un nodo Infura per la rete Sepolia
    infura_url = 'https://sepolia.infura.io/v3/5cb88f299e974e9082c695c5fb3e9b13'
    web3 = Web3(Web3.HTTPProvider(infura_url))  
   
    
    # Definizione del mittente e del destinatario
    indirizzo_mittente = '0x00C007CFf2bAe0b56d667e5ce421FBA83B007180'
    indirizzo_destinatario = '0x00C007CFf2bAe0b56d667e5ce421FBA83B007180'
    
    # Definizione delle chiavi private (questo è solo un esempio, non includere mai chiavi private in codice pubblico)
    chiave_privata = '48e059445b5d0ca44f6788d440b32f8e36333782f63e7a6c300574e82991e908'
    
    # Informazioni sull'acquisto
    nome_acquirente = 'Mario Rossi'
    data_acquisto = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    data=nome_acquirente+str(data_acquisto)
    # Ottieni il prezzo corrente del gas 
    gas_price = web3.eth.gas_price
    print('GAS', gas_price)
    # Costruzione della transazione
    transazione = {
        'nonce': web3.eth.get_transaction_count(indirizzo_mittente,'pending'),
        'to': indirizzo_destinatario,
        'value': web3.to_wei(0.001, 'ether'),  # Quantità di Ether di test da inviare
        'chainId': 11155111,
        'gas': 21480,
        'gasPrice': web3.to_wei('0.001', 'gwei'),  # Prezzo del gas
        'data': data.encode("utf-8").hex()  # Registrazione di nome e data
    }
    
    # Firma della transazione
    #transazione_firmata = web3.eth.account.sign_transaction(transazione, chiave_privata)
    
    # Invio della transazione
    #tx_hash = web3.eth.send_raw_transaction(transazione_firmata.rawTransaction)
    
    #print(str(web3.to_hex(tx_hash)))

prova()