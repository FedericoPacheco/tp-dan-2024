
import { lusitana } from '@/app/ui/fonts/fonts';
import FiltersForm from './filters-form';
import { fetchCategories } from '@/app/lib/data/producto/data';
export default async function ProductViewer() {  
    const categories = await (fetchCategories());
    return(
        <>
            <h1 className={`${lusitana.className} text-center text-[22px] text-black-50 border-4`}>
                <strong>Filtros de busqueda</strong>
            </h1>
            <FiltersForm categories={categories}/>
        </>
    );
}