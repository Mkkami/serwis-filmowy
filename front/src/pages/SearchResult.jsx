import { useEffect, useMemo, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { search } from "../api/api";
import Categories from "../components/Categories";
import "../styles/Results.css"

const initParams = {
    title: "",
    categoryIds: [],
    page: 0,
    sortBy: "title",
    sortDirection: "asc"
}

function MediaList() {
    const [searchParams, setSearchParams] = useSearchParams();
    const navigate = useNavigate();
    const [results, setResults] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [cateogries, setCategories] = useState(new Set());

    const currentParams = useMemo(() => {
        const title = searchParams.get('title') || initParams.title;
        const page = searchParams.get('page') || initParams.page;
        const sortBy = searchParams.get('sortBy') || initParams.sortBy;
        const sortDirection = searchParams.get('sortDirection') || initParams.sortDirection;

        const categoryIdsString = searchParams.get('categoryIds');
        const categoryIds = categoryIdsString ? categoryIdsString.split(',') : initParams.categoryIds;

        return {title, categoryIds, page, sortBy, sortDirection};
    }, [searchParams])

    useEffect(() => {
        // if (!currentParams.title) return;
        const fetchData = async () => {
            setIsLoading(true);
            const response = await search(searchParams);
            const data = await response;
            const cont = await data.json();

            setResults(cont.content);
            setIsLoading(false);
        }
        fetchData();
        
    }, [currentParams, searchParams])

    const updateSearch = (newParams) => {
        const updatedParams = new URLSearchParams(searchParams);

        Object.entries(newParams).forEach(([key, value]) => {
            if (Array.isArray(value)) {
                updatedParams.set(key, value.join(','));
            } else if (value !== null && value !== undefined && value !== "") {
                updatedParams.set(key, String(value));
            } else {
                updatedParams.delete(key);
            }
        })

        setSearchParams(updatedParams);
    }

    const updateCategories = (cats) => {
        const catIds = Array.from(cats).map(c => c.id);


        setCategories(cats);
        updateSearch({categoryIds: catIds, page: 0})
    }

    const updateSort = (newSortBy) => {
        const newDirection = currentParams.sortBy == newSortBy && currentParams.sortDirection === 'asc'
        ? "desc"
        : "asc";

        updateSearch({sortBy: newSortBy, sortDirection: newDirection, page: 0})
    }

    const sortDisplay = (el) => {
        if (el == searchParams.get('sortBy')) {
            if (searchParams.get('sortDirection') === "asc") {
                return <>&#9660;</>;
            } else {
                return <>&#9650;</>;
            }
            
        }
        return <>&#9660;</>;
    }

    const openDetails = (type, id) => {
        navigate(`/${type}/${id}`);
    }


    return (
        <div className="results-container">
            <div>
                <div className="sort">
                    <button onClick={() => updateSort('title')}>{sortDisplay('title')}Title</button>
                    <button onClick={() => updateSort('averageRating')}>{sortDisplay('averageRating')}Score</button>
                    <button onClick={() => updateSort('ratingCount')}>{sortDisplay('ratingCount')}review number</button>
                    <button onClick={() => updateSort('releaseYear')}>{sortDisplay('releaseYear')}year</button>
                
                </div>
                <div className="results">
                    {!isLoading ? results.map(res =>
                        <div key={res.id} className="media-card" onClick={() => openDetails(res.mediaType, res.id)} >
                            <span className="media-type">{res.mediaType}</span>
                            <div className="info">
                                <h3>{res.title}</h3>
                                <p className="release-year">{res.releaseYear}</p>
                            </div>
                            <p className="rating">{res.averageRating}/10 ({res.reviewCount})</p>
                        </div>
                    )
                    :
                    <p>Loading...</p>}
                </div>
            </div>
            <Categories style={{innerHeight: "1000px"}} selectedCategories={cateogries} setSelectedCategories={updateCategories}/>
        </div>
    )
}
export default MediaList;