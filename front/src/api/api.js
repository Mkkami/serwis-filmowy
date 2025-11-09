
export const getCategories = async () => {
    return await fetch("http://localhost:8080/public/category", {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        }
    });

}