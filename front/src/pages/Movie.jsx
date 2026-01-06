import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getFilm } from "../api/api";
import Reviews from "../components/Reviews";
import "../styles/Details.css"

function Movie() {
    const params = useParams();
    const [filmData, setFilmData] = useState(null);

    useEffect(() => {
        const getFilmDetails = async (id) => {
            const res = await getFilm(id);

            if (!res.ok) {
                return;
            }

            const data = await res.json();
            setFilmData(data);


        }
        getFilmDetails(params.id);
    }, [params])

    if (filmData === null) {
        return (
            <div className="info movie">
                <h3>Film not found</h3>
            </div>
        )
    }


    return (
        <div className="info movie">
            <h1>{filmData.title}</h1>
            <div className="details">
                <p>Duration: {filmData.duration} min</p>
                <p>Release year: {filmData.releaseYear}</p>
                <p>Categories: {filmData.categories.map(cat => {
                    return (cat.name + ", ")
                })}</p>
            </div>
            <Reviews reviews={filmData.reviews} type="film" id={filmData.id} />
        </div>
    )
}
export default Movie;