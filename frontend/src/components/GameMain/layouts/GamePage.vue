<template>
  <div
    class="row"
    id="session">
    <div class="game-layout col-9">
      <!-- 위쪽 유저 -->
      <div class="row h-30 user-video user-video-head row-cols-3 g-2 g-lg-3">
        <JangSaKkun
          :stream-manager="publisher"
          :player="true" />
        <JangSaKkun 
          :stream-manager="this.subscribers[3]"
          :index="3" />
        <JangSaKkun
          :stream-manager="this.subscribers[0]"
          :index="0" />
      </div>
      <!-- 테이블 -->
      <div class="row h-30 game-table-el">
        <div v-if="this.$store.state.gamePossible">
          <GameStartInfo />
        </div>
        <div v-else>
          <GameWaiting 
            @go-to-main="leaveSession" />
        </div>
        <!-- @gamePossible="gamestart" -->
        <!-- <GameLogicTest4Abilities /> -->
      </div>

      <!-- 아래쪽 유저 -->
      <div class="row h-30 user-video user-video-foot row-cols-3 g-2 g-lg-3">
        <JangSaKkun
          :stream-manager="this.subscribers[1]"
          :index="1" />
        <JangSaKkun
          :stream-manager="this.subscribers[4]"
          :index="4" />
        <JangSaKkun
          :stream-manager="this.subscribers[2]"
          :index="2" />
      </div>
    </div>


    <div class="col-3">
      <div class="row">
        <div class="game-log">
          <GameLog />
        </div>
      </div>
      <div class="row">
        <div class="game-chatting">
          <GameChat />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import GameWaiting from '@/components/GameMain/layouts/GameWaiting.vue'
import GameStartInfo from '@/components/GameMain/layouts/GameStartInfo.vue'
import JangSaKkun from '@/components/GameMain/modules/JangSaKkun.vue'; 
import GameChat from '@/components/GameMain/layouts/GameChat.vue'; 
import GameLog from '@/components/GameMain/layouts/GameLog.vue'; 
// import GameLogicTest4Abilities from '@/components/GameMain/modules/GameLogicTest4Abilities';
import axios from 'axios';
import { OpenVidu } from 'openvidu-browser';
import { mapActions } from 'vuex'
axios.defaults.headers.post['Content-Type'] = 'application/json';

//const OPENVIDU_SERVER_URL = "https://" + location.hostname + ":4443";
const OPENVIDU_SERVER_URL = "https://i6b208.p.ssafy.io:5443";
const OPENVIDU_SERVER_SECRET = "ssafy";

export default {
  data() {
    return {
			nickName : '',
      
			// openvidu
			gamePossible: false,
      OV: undefined,
			session: undefined,
			mainStreamManager: undefined,
			publisher: undefined,
      subscribers: [],

			mySessionId: this.$route.params.code,
			myUserName: this.$store.getters.nickName,
    }
  },
  components: {
    GameWaiting,
    GameStartInfo,
    JangSaKkun,
		GameChat,
		GameLog,
  },
	
  mounted() {
    this.joinSession();
		this.emitter.on('gameStarted', this.setGamePossibleTrue)
  },
  methods: {
	setPlayerName(){
		this.setNickName(this.nickName)
		this.emitter.emit('connect')
	},
	...mapActions([
			"changeGamePossible",
			"setNickName"
		]),
	// store의 gamePossible을 true로 변경
	setGamePossibleTrue() {
		this.changeGamePossible(true)
	},
	// OpenVidu System 
    joinSession() {
      // --- Get an OpenVidu object ---
			this.OV = new OpenVidu();

      // --- Init a session ---
			this.session = this.OV.initSession();

      // --- Specify the actions when events take place in the session ---

      // On every new Stream received...
			this.session.on('streamCreated', ({ stream }) => {
				const subscriber = this.session.subscribe(stream);
				this.subscribers.push(subscriber);
			});

      // On every Stream destroyed...
			this.session.on('streamDestroyed', ({ stream }) => {
				const index = this.subscribers.indexOf(stream.streamManager, 0);
				if (index >= 0) {
					this.subscribers.splice(index, 1);
				}
			});

      // On every asynchronous exception...
			this.session.on('exception', ({ exception }) => {
				console.warn(exception);
			});
      
      // --- Connect to the session with a valid user token ---

			// 'getToken' method is simulating what your server-side should do.
			// 'token' parameter should be retrieved and returned by your own backend
			this.getToken(this.mySessionId).then(token => {
				this.session.connect(token, { clientData: this.myUserName })
					.then(() => {
						console.log(this.myUserName)
						// --- Get your own camera stream with the desired properties ---

						let publisher = this.OV.initPublisher(undefined, {
							audioSource: undefined, // The source of audio. If undefined default microphone
							videoSource: undefined, // The source of video. If undefined default webcam
							publishAudio: true,  	// Whether you want to start publishing with your audio unmuted or not
							publishVideo: true,  	// Whether you want to start publishing with your video enabled or not
							resolution: '320x240',  // The resolution of your video
							frameRate: 30,			// The frame rate of your video
							insertMode: 'APPEND',	// How the video is inserted in the target element 'video-container'
							mirror: true       	// Whether to mirror your local video or not
						});

						this.mainStreamManager = publisher;
						this.publisher = publisher;

						// --- Publish your stream ---

						this.session.publish(this.publisher);
					})
					.catch(error => {
						console.log('There was an error connecting to the session:', error.code, error.message);
					});
			});

			window.addEventListener('beforeunload', this.leaveSession)
		},

    leaveSession () {
			// --- Leave the session by calling 'disconnect' method over the Session object ---
			if (this.session) this.session.disconnect();

			this.session = undefined;
			this.mainStreamManager = undefined;
			this.publisher = undefined;
			this.subscribers = [];
			this.OV = undefined;

			window.removeEventListener('beforeunload', this.leaveSession);
      this.$router.push({ name: 'Home' })
		},

    updateMainVideoStreamManager (stream) {
			if (this.mainStreamManager === stream) return;
			this.mainStreamManager = stream;
		},

		getToken (mySessionId) {
			return this.createSession(mySessionId).then(sessionId => this.createToken(sessionId));
		},

		// See https://docs.openvidu.io/en/stable/reference-docs/REST-API/#post-session
		createSession (sessionId) {
			return new Promise((resolve, reject) => {
				axios
					.post(`${OPENVIDU_SERVER_URL}/openvidu/api/sessions`, JSON.stringify({
						customSessionId: sessionId,
					}), {
						auth: {
							username: 'OPENVIDUAPP',
							password: OPENVIDU_SERVER_SECRET,
						},
					})
					.then(response => response.data)
					.then(data => resolve(data.id))
					.catch(error => {
						if (error.response.status === 409) {
							resolve(sessionId);
						} else {
							console.warn(`No connection to OpenVidu Server. This may be a certificate error at ${OPENVIDU_SERVER_URL}`);
							if (window.confirm(`No connection to OpenVidu Server. This may be a certificate error at ${OPENVIDU_SERVER_URL}\n\nClick OK to navigate and accept it. If no certificate warning is shown, then check that your OpenVidu Server is up and running at "${OPENVIDU_SERVER_URL}"`)) {
								location.assign(`${OPENVIDU_SERVER_URL}/accept-certificate`);
							}
							reject(error.response);
						}
					});
			});
		},

		// See https://docs.openvidu.io/en/stable/reference-docs/REST-API/#post-connection
		createToken (sessionId) {
			return new Promise((resolve, reject) => {
				axios
					.post(`${OPENVIDU_SERVER_URL}/openvidu/api/sessions/${sessionId}/connection`, {}, {
						auth: {
							username: 'OPENVIDUAPP',
							password: OPENVIDU_SERVER_SECRET,
						},
					})
					.then(response => response.data)
					.then(data => resolve(data.token))
					.catch(error => reject(error.response));
			});
		},
  },
}
</script>

<style scoped>

</style>