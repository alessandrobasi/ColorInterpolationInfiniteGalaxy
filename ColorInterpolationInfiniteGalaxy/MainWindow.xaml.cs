using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace ColorInterpolationInfiniteGalaxy {
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window {

        public MainWindow() {
            InitializeComponent();
        }

        private (byte, byte, byte) interpolateColor(Color color1, Color color2, float num, float i) {
            // https://stackoverflow.com/questions/13488957/interpolate-from-one-color-to-another#answer-21010385
            float step(float num, float q) => (q+1)/num;

            return (
                (byte)((color1.R - color2.R) * step(num, i) + color2.R),
                (byte)((color1.G - color2.G) * step(num, i) + color2.G),
                (byte)((color1.B - color2.B) * step(num, i) + color2.B)
            );
            
        }


        private List<Color> GetColors(Color colore1, Color colore2, int num) {
            List<Color> colors = new List<Color>();
            colors.Add(colore2);
            for (int i = 0; i < num; i++) {
                byte r, g, b;
                (r, g, b) = interpolateColor(colore1, colore2, num, i);
                colors.Add(Color.FromRgb(r, g, b));
            }
            colors.Add(colore1);

            return colors;
        }

        private string ToHexString(Color c) { return $"{c.R:X2}{c.G:X2}{c.B:X2}"; }

        private void inTesto_TextChanged(object sender, TextChangedEventArgs e) {

            TextBox inTesto = sender as TextBox;

            int num_char = inTesto.Text.Length-2;
            if (num_char < 1) { return; }

            Color? colore1 = color1.SelectedColor;
            Color? colore2 = color2.SelectedColor;
            if (!colore1.HasValue || !colore2.HasValue) { return; }

            List<Color> listacolore = GetColors(colore1.Value, colore2.Value, num_char);

            outTesto.Children.Clear();
            foreach ((char c, Color q) in inTesto.Text.Select( (i,q) => (i, listacolore[q] ) )) {

                TextBlock a = new TextBlock();
                a.Text = c.ToString();
                SolidColorBrush colore = new SolidColorBrush();
                colore.Color = q;
                a.Foreground = colore;
                outTesto.Children.Add(a);
            }

        }

        private void copyBtn_Click(object sender, RoutedEventArgs e) {

            String Result="";

            int num_char = inTesto.Text.Length-2;
            if (num_char < 1) { return; }

            Color? colore1 = color1.SelectedColor;
            Color? colore2 = color2.SelectedColor;
            if (!colore1.HasValue || !colore2.HasValue) { return; }

            List<Color> listacolore = GetColors(colore1.Value, colore2.Value, num_char);

            foreach ((char c, Color q) in inTesto.Text.Select((i, q) => (i, listacolore[q]))) {

                Result += String.Format("[{0}]{1}[-]", ToHexString(q), c);

            }

            Clipboard.SetText(Result);

        }
    }
}
