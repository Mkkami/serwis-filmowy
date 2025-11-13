import { useEffect, useMemo, useState } from "react";
import { useLocation, useNavigate, useSearchParams } from "react-router-dom";
import { search } from "../api/api";
import Categories from "../components/Categories";

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

            console.log(data);

            setResults(data.content);
            setIsLoading(false);
        }
        fetchData();
        
    }, [currentParams, searchParams, ])

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


    return (
        <div>
            <h1>LISTTTT</h1>
            <Categories selectedCategories={cateogries} setSelectedCategories={updateCategories}/>
        </div>
    )
}
export default MediaList;