import axios from "axios";

const requests = axios.create({
    // baseURL: 'http://localhost:8002/',
    baseURL:"/api",
    timeout: 30000,
})

const jwt = localStorage.getItem('token')
requests.interceptors.request.use((config) => {
    //每次访问携带凭证
    if (jwt) {
        config.headers['authorization'] = jwt;
    }
    return config;
})

//设置响应拦截器，一个成功的回调函数，和一个失败的回调函数
requests.interceptors.response.use((resolve) => {
        const jwt = resolve.headers['authorization']
        if (jwt) {
            localStorage.setItem('token', jwt);
        }
        //resolve是一个对象，内部包装了许多数据，这里直接返回数据data
        return resolve.data;
    },
    error => {

    }
)
//对外暴露
export default requests;







