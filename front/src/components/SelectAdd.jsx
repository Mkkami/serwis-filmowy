import { useNavigate } from "react-router-dom";
import "../styles/SelectAdd.css"
import { useEffect } from "react";

function SelectAdd({handleClose}) {
    const navigate = useNavigate();

    useEffect(() => {
        console.log("addddd");
    })

    const handleMovieClick = () => {
        handleClose();
        navigate("/add/movie");
    }

    const handleSeriesClick = () => {
        handleClose();
        navigate("/add/series");
    }

    return (
        <dialog open>
            <button className="close-btn" onClick={handleClose}>X</button>
            <div>
                <button onClick={handleMovieClick}>Movie</button>
                <button onClick={handleSeriesClick}>Series</button>
            </div>
        </dialog>
    )
}
export default SelectAdd;