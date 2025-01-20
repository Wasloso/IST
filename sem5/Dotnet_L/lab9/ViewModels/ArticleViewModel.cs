using System.ComponentModel.DataAnnotations;
namespace lab9.ViewModels
{
    public enum ArticleCategory
    {
        Food,
        Electronics,
        Books,
        Decorations,
        Garden,
        Other,
    }
    public class ArticleViewModel
    {
        public int Id { get; set; }
        [Required]
        [MaxLength(40)]
        public string Name { get; set; } = "";
        [Required]
        [Range(0, double.MaxValue)]
        [DisplayFormat(DataFormatString = "{0:C}")]
        public Decimal Price { get; set; }
        [Required]
        [DataType(DataType.Date)]
        [DisplayFormat(DataFormatString = "{0:yyyy-MM-dd}", ApplyFormatInEditMode = true)]
        [Display(Name = "Expires")]
        public DateTime ExpiresOn { get; set; }
        [Required]
        public ArticleCategory Category { get; set; }

    }




}