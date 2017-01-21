#!/usr/bin/python

"""
    Simple socket server using threads aimed for chatterbean access over telnet.
"""
 
import socket
import sys
import getopt

from thread import *
from subprocess import Popen, PIPE, STDOUT

# Cleansing argument list from malformed options at the beginning, by cutting out first arguments such process name.
for argument in sys.argv:
	if argument.startswith("-") or argument.startswith("--"):
		valid_arguments = sys.argv[sys.argv.index(argument):]
		break;

# Gather options from command line for apprioprate set up. 
try:
	options, arguments = getopt.getopt(valid_arguments, "p:", ["port="])
except getopt.GetoptError:
      print 'Provide port numer to listen on using -p or --port.'
      sys.exit(1)

for option, option_argument in options:
	if option in ("-p", "--port"):
		HOST = ''   					# Symbolic name meaning all available interfaces.
		PORT = int(option_argument)  	# Arbitrary non-privileged port chosen by user.
		 
		# Choose address family for socket and create it.
		server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		print 'Socket created'
		 
		# Bind socket to local host and port.
		try:
		    server_socket.bind((HOST, PORT))
		except socket.error as msg:
			print 'Bind failed. Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
			sys.exit(2)
		     
		print 'Socket bind complete'
		 
		# Start listening on socket. Maxiumum number of queued connection is set to 10.
		server_socket.listen(10)
		print 'Socket now listening'
		 
		# Function for handling connections. This will be used to create threads.
		def client_thread(connection):
			# Sending message to connected client.
			connection.send('Welcome to the chat server. Type something and hit enter\n') #Send only takes string.
		     
			# New subprocess is created from one command with in and out pipes.
			chatterbean = Popen(['/home/siata/Workspace/jdk1.8.0_73/bin/java -jar chatterbean.jar'], shell=True, stdout=PIPE, stdin=PIPE)  

			# Infinite loop so that function do not terminate and thread do not end.
			while True:
			   
				# Receiving data from client. Blocking call.
				data = connection.recv(1024)

				# If not data is sent break infinite loop and kill subprocess.						
				if not data: 
					chaterrbean.kill()
					break

				# Write query typed by the user.
				chatterbean.stdin.write(data)

				# Active listening in case suprocess wasn't able to generate result in time.
				while True:
					answer = chatterbean.stdout.readline()
					if answer != '':
			  			connection.sendall(answer)
						break
					
			# Came out of loop.
			connection.close()
		 
		# Now keep talking with the client.
		while True:
		    # Wait to accept a connection - blocking call.
		    connection, address = server_socket.accept()
		    print 'Connected with ' + address[0] + ':' + str(address[1])
		     
		    # Start new thread takes 1st argument as a function name to be run, second is the tuple of arguments to the function.
		    start_new_thread(client_thread, (connection,))
		 
		# Closing socket if something went wrong.
		server_socket.close()

