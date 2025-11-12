import { useState, useEffect } from "react";

function Search() {
    const [title, setTitle] = useState("");

    useEffect(() => {
        console.log(title);
    }, [title])

    const handleInput = (e) => {
        setTitle(e.target.value);
    }

    return (
        <>

            <form action="" className="form-search-bar">
                <input className='search-bar' placeholder='Search' onInput={handleInput}></input>
                <button type="submit">s</button>
            </form>
        </>
    )
}
export default Search;