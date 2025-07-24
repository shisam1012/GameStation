//Using REST-API to send the register data to the server

export function submitRegisterData({ username, password }) {
    const data = new URLSearchParams({
        username,
        password, //still wothout hashing
    });

    return fetch(`http://localhost:8080/api/register?${data.toString()}`, {
        method: 'GET',
    }).then((res) => {
        if (!res.ok) throw new Error('Server error');
        return res.json();
    });
}

/*export function submitRegisterData({ username, password }) {
    return fetch('http://localhost:8080/api/register', {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            username,
            password,
        }),
    }).then((res) => {
        if (!res.ok) throw new Error('Server error');
        return res.json();
    });
}*/
