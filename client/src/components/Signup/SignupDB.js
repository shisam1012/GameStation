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
        const data = await res.json();

        if (!res.ok) {
            throw new Error(data.error || 'Unknown server error');
        }

        return data;
    });
}
