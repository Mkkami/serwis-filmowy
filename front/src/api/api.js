
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

export const addNewSeries = async (title, releaseYear, endYear, categories) => {
    return await fetch("http://localhost:8080/series", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            title: title,
            releaseYear: releaseYear,
            endYear: endYear,
            categories: categories
        }),
        credentials: "include"
    })
}

export const getFilm = async (id) => {
    return await fetch(`http://localhost:8080/film/${id}`, {
        method: "GET",
        credentials: "include"
    })
}

export const getSeries = async (id) => {
    return await fetch(`http://localhost:8080/series/${id}`, {
        method: "GET",
        credentials: "include"
    })
}

export const search = async (url) => {
    return await fetch(`http://localhost:8080/search?${url}`, {
    });
}