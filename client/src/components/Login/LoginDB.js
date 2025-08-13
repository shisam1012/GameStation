//Using REST-API to send the register data to the server

export function submitLoginData({ username, password }) {
    const data = new URLSearchParams({
        username,
        password, //still wothout hashing
    });

    return fetch(`http://localhost:8080/api/login?${data.toString()}`, {
        method: 'GET',
    }).then(async (res) => {
        const data = await res.json();

        if (!res.ok) {
            throw new Error(data.error || 'Unknown server error');
        }

        return data;
    });
}
