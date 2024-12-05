import http from 'k6/http';
import {check, sleep} from 'k6';
import {Rate} from 'k6/metrics';
import {URL} from 'https://jslib.k6.io/url/1.0.0/index.js';


const BASE_URL = 'http://localhost:8080';
export let errorRate = new Rate('errors');

export let options = {
    stages: [
        {duration: '1s', target: 50},  // 1초 동안 50유저까지 증가 합니다.
        {duration: '5s', target: 50},  // 5초 동안 50유저로 유지 됩니다.
        {duration: '1s', target: 0},   // 1초 동안 0 유저까지 감소합니다.
    ]
};

export default function () {
    const url = new URL(`${BASE_URL}/api/employee?page=0&pageSize=10`).toString()

    const headers = {'Content-Type': 'application/json'};
    const response = http.get(url, {headers: headers});

    let result = check(response, {
        'is status 200'      : (r) => r.status === 200,
        'is duration < 500ms': (r) => r.timings.duration < 500,
    });

    if (response.status === 200) {
        console.info(`Success : ${response.body}`);
    } else {
        console.error(`Error : ${response} - ${response.body}`);
    }

    errorRate.add(!result);
    sleep(1);
}