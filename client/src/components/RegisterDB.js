export function submitRegisterData({ username, password }) {
    return fetch('http://localhost:8080/api/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            username,
            password,
        }),
    }).then((res) => {
        if (!res.ok) throw new Error('Server error');
        return res.json();
    });
}
