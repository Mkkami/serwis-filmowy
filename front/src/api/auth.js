export const logout = async () => {
    await fetch("http://localhost:8080/logout", {
        method: "POST",
        credentials: "include"
    });
};