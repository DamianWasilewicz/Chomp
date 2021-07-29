
/**
 * models a textbox for route.
 */
function Textbox(props) {

    const update = (event) => {
        props.change(event.target.value);
    }

    return (
        <div className="text-box">
            <div>
                <label> {props.label} </label>
            </div>
            <div>
                <input
                    type={'text'}
                    value={props.clicked}
                    onChange={update}
                />
            </div>

        </div>
    );
}

export default Textbox;