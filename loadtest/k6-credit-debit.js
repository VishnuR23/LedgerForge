
import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
  vus: 50,
  duration: '30s',
};

export default function () {
  const id = 'acct-' + Math.floor(Math.random()*100);
  http.post('http://localhost:8080/api/accounts/' + id + '/credit', JSON.stringify({amountCents: 100}), { headers: { 'Content-Type': 'application/json' } });
  http.post('http://localhost:8080/api/accounts/' + id + '/debit', JSON.stringify({amountCents: 50}), { headers: { 'Content-Type': 'application/json' } });
  sleep(0.1);
}
