
export const logout = async () => {
    await fetch("http://localhost:8080/logout", {
        method: "POST",
        credentials: "include"
    });
};

export const login = async (username, password) => {
    return await fetch("http://localhost:8080/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: new URLSearchParams({
            username: username,
            password: password,
        }),
        credentials: "include",
    })
}

export const register = async (username, password) => {
    return await fetch("http://localhost:8080/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            username: username,
            password: password,
        }),
    })
}