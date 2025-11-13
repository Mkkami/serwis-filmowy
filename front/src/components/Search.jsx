import { useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";

const params = {
    title: "",
    categoryIds: [],
    page: 0,
    sortBy: "title",
    sortDirection: "asc"
}

function Search() {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    const initialQuery = searchParams.get('title') || '';
    const [title, setTitle] = useState(initialQuery);

    useEffect(() => {
        setTitle(searchParams.get('title') || '');
    }, [searchParams])

    const handleInput = (e) => {
        setTitle(e.target.value);
    }

    const handleSubmit = (e) => {
        e.preventDefault();
        if (title.trim()) {
            navigate(`/search?title=${encodeURIComponent(title.trim())}`)
        } else {
            navigate('/search')
        }
    }

    return (
        <>
            <form className="form-search-bar" onSubmit={handleSubmit}>
                <input className='search-bar' placeholder='Search' onChange={handleInput}></input>
                <button type="submit">s</button>
            </form>
        </>
    )
}
export default Search;