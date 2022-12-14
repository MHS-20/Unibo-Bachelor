'use strict';

class App extends React.Component {
  constructor(){
        super();
        this.state = {
        operation: "",
        result: ""
        }
        this.onClick = this.onClick.bind(this); 
  }

  onClick(e) {
        let button = e.target.name
        if(button === "="){
            this.calculate()
        }

      else if(button === "C"){
          this.reset()
      }
      else if(button === "CE"){
          this.backspace()
      }
        else if(button === "sqrt"){
         this.setState({
            operation: this.state.operation + 'Math.sqrt('
              })
        } else if(button === "loge"){
         this.setState({
            operation: this.state.operation + 'Math.log('
              })
        } else if(button === "e^x"){
         this.setState({
            operation: this.state.operation + 'Math.pow(E'
              })
        }else if(button === "1/x"){
         this.setState({
            operation: this.state.operation + '1/x'
              })
      }
      else {
          this.setState({
        operation: this.state.operation + button
          })
      }
  };

  calculate() {
        try {
              this.setState({
              result: (eval(this.state.operation) || "" ) + ""
            })
        } catch (e) {
            this.setState({
          result: "error"
            })

        }
  };

  reset(){
      this.setState({
          operation: "",
          result: ""
      })
        };


  backspace(){
      this.setState({
          operation: this.state.operation.slice(0, -1)
      })
  };

  //
  calculateAJAX () {  
    requestCalculation(
      `./calculatorServlet?op=${operation}`, //url in web.xml + param
      (res) => {
        this.setState({result: res})
      }
    );
  }

  render() {
      return (
        <div className="calculator-body">
            <h1>Calcolatrice</h1>
            <Display result={this.state.operation}/> 
            <Display result={this.state.result}/>
            <KeyBoard onClick={this.onClick}/>
        </div>

      );
  }
}
