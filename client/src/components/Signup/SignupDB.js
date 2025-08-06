export function submitSignupData({ username, email, password }) {
    return fetch('http://localhost:8080/api/signup', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            username,
            email,
            password,
        }),
    }).then(async (res) => {
        if (!res.ok) {
            const errorText = await res.text();
            throw new Error(errorText || 'Unknown server error');
        }
        return res.json();
    });
}
