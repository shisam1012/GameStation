export function submitSignupData({ username, email, password }) {
    return fetch('http://localhost:8080/api/signup', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            username,
            email,
            password,
        }),
    }).then((res) => {
        if (!res.ok) throw new Error('Server error');
        return res.json();
    });
}
