# 22.02.09

## Front 배포

[참고 블로그](https://dev-r.tistory.com/11)

- npm 설치
- npm i 명령어로 설치 도중 에러, `npm i @vue/cli-service`로 의존성 추가
- 해결 안됨, `sudo npm i -g @vue/cli` 실행 후 `rm -rf node_modules package-lock.json && npm install` 실행
- ` bootstrap@5.1.3 requires a peer of @popperjs/core@^2.10.2 but none is installed. You must install peer dependencies yourself.`라는 문구 발견. 이것때문인가?
- `npm i @popperjs/core@^2.10.2` 실행
- 안됨
- `npm WARN notsup Unsupported engine for sass@1.49.7: wanted: {"node":">=12.0.0"} (current: {"node":"10.19.0","npm":"6.14.4"})` 버전 문제인가..? [node 업데이트](https://jsikim1.tistory.com/158)해봄
- 설치는 됐는데.. ` Cannot read properties of undefined (reading 'NormalModule')` 에러가 뜸
- 삭제하고 다시 풀 땡겨서 npm run serve
- `Error: ENOSPC: System limit for number of file watchers reached, watch '/home/ubuntu/S06P12B208/frontend/node_modules/babel-loader/lib/injectCaller.js'` 에러
- `echo fs.inotify.max_user_watches=524288 | sudo tee -a /etc/sysctl.conf && sudo sysctl -p` 기입 
- run은 됐는데, 외부 접속 허용이 안되어있어서 에러가 나는 듯 했음
- 함부로 포트를 뚫을 순 없기에 ufw로만 살짝 해제했는데 그래도 접속이 되지 않음
- 내일 싸피에서 알려준 대로 해야겠다..
