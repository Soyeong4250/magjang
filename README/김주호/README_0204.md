# 22.02.04

## Back API 완성

유저 이메일이 db에 있다면 해당 유저를, 없다면 새 유저를 추가하고 반환하도록 했다.

```java
public User findUserUsingEmail(String email) {
    return (User)this.userRepository.findByEmail(email).orElseGet(() -> {
        return this.JoinNewUserWithEmail(email);
    });
}

public User JoinNewUserWithEmail(String email) {
    User user = new User();
    user.setEmail(email);
    this.userRepository.save(user);
    return user;
}
```



닉네임이 존재하지 않는다면 닉네임을 기입하게끔 하고, 해당 닉네임의 존재유무에 따라 다르게 동작하도록 했다.

코드가 깔끔하진 않아서 불만족스럽다.

```java
public String setNickName(Long userId, String nickName) {
    if (this.userRepository.findByNickName(nickName).isPresent()) {
        return "이미 존재하는 닉네임입니다!";
    } else {
        Optional<User> user = this.userRepository.findById(userId);
        if (user.isPresent()) {
            user.ifPresent((u) -> {
                u.setNickName(nickName);
            });
            return "true";
        } else {
            return "유저 데이터 오류! 관리자에게 문의하세요";
        }
    }
}
```



## Front axios 요청 및 컴포넌트 구조 완성

닉네임 생성 부분에서, 해당하는 input이 컴포넌트 내에 따로 있으므로 props와 emit을 준비해둬야 했다.

### inputText.vue

```javascript
@keypress.enter="$emit('enter')"
@keyup="$emit('child-input-change', inputText)"
@input="inputText = $event.target.value"
```

```javascript
export default {
  props: {
    placeText: {
      type: String,
      default: "default"
    },
  },
  data() {
    return {
      inputText: "",
    }
  },
  emits: ['child-input-change', 'enter'],
}
```



그 후 닉네임을 입력하는 곳에서 이벤트를 걸어 주어 제대로 동작되게끔 했다.

### CreateNickname.vue

```html
<template>
  <div>
    <InputText
      place-text="닉네임을 입력해 주세요" 
      @child-input-change="nickNameChanged"
      @enter="nickNameCreated" />
    <p class="text-white">
      {{ warning }}
    </p>
    <Buttons
      btn-text="생성"
      @click="nickNameCreated" />
  </div>
</template>
```

```javascript
nickNameChanged(inputData) {
    this.nickName = inputData;
},
nickNameCreated() {
    axios.post('/account/signup', {
        "nickName": this.nickName,
        "userId": this.$store.getters.userId
    })
        .then(response => {
        console.log(response.data);  // 받는 거에 따라 닉네임 입력할지 안할지
        if (response.data == true) {
            this.setNickName(response.data)
        } else {
            this.warning = response.data
        }
    })
        .catch(error =>{
        console.log(error)
    });
}
```

api와 요청 구조를 완성시킨 셈이 되었다.