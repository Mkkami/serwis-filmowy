import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getSeries } from "../api/api";
import Reviews from "../components/Reviews";
import "../styles/Details.css"
import Episodes from "../components/Episodes";

function Series() {
    const params = useParams();
    const [seriesData, setSeriesData] = useState(null);

    useEffect(() => {
        const getSeriesDetails = async (id) => {
            const res = await getSeries(id);

            if (!res.ok) {
                return;
            }

            const data = await res.json();
            setSeriesData(data);


        }
        getSeriesDetails(params.id);
        console.log(seriesData)
    }, [params])


    if (seriesData === null) {
        return (
            <div className="info series">
                <h3>Series not found</h3>
            </div>
        )
    }

    return (
        <div className="info series">
            <h1>{seriesData.title}</h1>
            <div className="details">
                <p>Release year: {seriesData.releaseYear}</p>
                <p>End year: {seriesData.endYear}</p>
                <p>Categories: {seriesData.categories.map(cat => {
                    return (cat.name + ", ")
                })}</p>
            </div>
            <Episodes id={seriesData.id} />
            <Reviews reviews={seriesData.reviews} type={"series"} id={seriesData.id} />
        </div>
    )
}
export default Series;