First time setup
- install ngrok (https://ngrok.com/download)
- create an account for ngrok
- ./ngrok authtoken $authtoken

To run the server
- start the server: `./gradlew :run --args "server"`
- start ngrok in another terminal: `ngrok/ngrok http 8080 -bind-tls=true -auth="$username:$password"`
- ngrok will provide urls for https traffic. This can be found on localhost:4040/status or in the terminal
- needed for clients to run: ngrok url and authentication

To run a client
- create an ai for a game (by extending `AiPlayer`)
- add that ai to the `availablePlayers` in the `PlayerHolder`
- start ngrok: `ngrok/ngrok http 8081 -bind-tls=true -auth="$username:$password"`
- start the client in another terminal: `gw :run --args "client -c ownUser:ownPass -C serverUser:serverPass -e ownNgrokUrl -E serverNgrokUrl -n userName -k sharedKey"`
- register at the server: `curl ownNgrokUrl/register -X POST`
- to join a tournament, find out its name, then `curl ownNgrokUrl/tournament/$name/join -X POST`