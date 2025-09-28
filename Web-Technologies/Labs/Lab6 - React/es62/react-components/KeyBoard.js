'use strict';

class KeyBoard extends React.Component {

    //loge(x), sqrt(x), e^x, 1/x

    render() {
        return (
            <div className="keypad">
            <button name="(" onClick={this.props.onClick}>(</button>
            <button name="CE" onClick={this.props.onClick}>CE</button>
            <button name=")" onClick={this.props.onClick}>)</button>
            <button name="C" onClick={this.props.onClick}>C</button>
            <br/>


            <button name="1" onClick={this.props.onClick}>1</button>
            <button name="2" onClick={this.props.onClick}>2</button>
            <button name="3" onClick={this.props.onClick}>3</button>
            <button name="+" onClick={this.props.onClick}>+</button>
            <br/>

            <button name="4" onClick={this.props.onClick}>4</button>
            <button name="5" onClick={this.props.onClick}>5</button>
            <button name="6" onClick={this.props.onClick}>6</button>
            <button name="-" onClick={this.props.onClick}>-</button><br/>

            <button name="7" onClick={this.props.onClick}>7</button>
            <button name="8" onClick={this.props.onClick}>8</button>
            <button name="9" onClick={this.props.onClick}>9</button>
            <button name="*" onClick={this.props.onClick}>x</button>
            <br/>

            <button name="." onClick={this.props.onClick}>.</button>
            <button name="0" onClick={this.props.onClick}>0</button>
            <button name="=" onClick={this.props.onClick}>=</button>
            <button name="/" onClick={this.props.onClick}>÷</button>
           <br/>

            <button name="1/x" onClick={this.props.onClick}> 1/x</button>
            <button name="e^x" onClick={this.props.onClick}>e^x</button>
            <button name="loge" onClick={this.props.onClick}>loge</button>
            <button name="sqrt" onClick={this.props.onClick}>sqrt</button>
            </div>
        );
    }
}
