import { useEffect, useState } from "react";
import { getCategories } from "../api/api";
import "../styles/Categories.css"

function Categories() {
    const [isLoading, setIsLoading] = useState(true);
    const [categories, setCategories] = useState([]);

    useEffect(() => {
        const fetchCategories = async () => {
            const res = await getCategories();
            const data = await res.json();
            console.log(data);
            setCategories(data);
            setIsLoading(false);
        }
        fetchCategories();
    }, []);

    return (
        <div className="categories">
            {isLoading ? <p>Loading...</p>
        :
            categories.map((cat) =>
                <label key={cat.id} htmlFor={`cat-${cat.id}`}>
                    <input id={`cat-${cat.id}`} type="checkbox" name={cat.name} />
                    {cat.name}
                </label>

            )}                
        </div>
    )
}
export default Categories;