import { useEffect, useState } from "react";
import { getEpisodes } from "../api/api";
import "../styles/Episode.css"

function Episodes({id}) {
    const [open, setOpen] = useState(false);
    const [data, setData] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            const res = await getEpisodes(id);
            const dat = await res.json();
            setData(dat);
        }
        fetchData();
    }, [])

    const showMore = () => {
        setOpen(prev => !prev);
    }

    return (
        <div className={`episode-container ${open ? "open" : ""}`}>
            <button onClick={showMore} className="episode-btn"><h2>Episodes</h2></button>
            {open && data.map(e => 
                <div className="episode-card" key={e.id}>
                    <p>{e.episodeNumber}</p>
                    <p>{e.title}</p>
                    <p>{e.releaseDate}</p>
                </div>
            )}
        </div>
    )
}
export default Episodes;