
export const getCategories = async () => {
    return await fetch("http://localhost:8080/public/category", {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        }
    });

}

export const addNewFilm = async (title, duration, releaseYear, categories) => {
    return await fetch("http://localhost:8080/film", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            title: title,
            duration: duration,
            releaseYear: releaseYear,
            categories: categories
        }),
        credentials: "include"
    })
}